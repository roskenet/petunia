'use client';

import { useEffect, useState } from 'react';

export function useAuthenticatedFetch<T>(url: string) {
    const [data, setData] = useState<T | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}${url}`, {
            credentials: 'include',
        })
            .then((res) => {
                if (res.status === 401 || res.redirected) {
                    window.location.href = `${process.env.NEXT_PUBLIC_API_BASE_URL}/oauth2/authorization/keycloak`;
                    return null;
                }
                return res.json();
            })
            .then((data) => {
                if (data) setData(data);
            })
            .catch((err) => {
                setError(err.message);
            })
            .finally(() => {
                setLoading(false);
            });
    }, [url]);

    return { data, error, loading };
}

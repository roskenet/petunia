'use client';

import { useEffect, useState } from 'react';

export function useAuthenticatedFetch<T>(url: string) {
    const [data, setData] = useState<T | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch(url, {
            credentials: 'include',
        })
            .then((res) => {
                if (res.status === 401 || res.redirected) {
                    // User not logged in – redirect to login via BFF
                    // window.location.href = url;
                    window.location.href = "/oauth2/authorization/keycloak";
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

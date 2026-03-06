'use client';

import { useEffect, useState } from 'react';
import { requestJson } from '@/lib/api';

export function useAuthenticatedFetch<T>(url: string) {
    const [data, setData] = useState<T | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setLoading(true);
        requestJson<T>(url)
            .then((data) => {
                setData(data);
                setError(null);
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

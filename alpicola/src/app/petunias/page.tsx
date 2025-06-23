'use client';

import { useAuthenticatedFetch } from '@/lib/hooks/useAuthenticatedFetch';

type Petunia = {
    name: string;
    image_url: string;
};

export default function PetuniasPage() {
    const { data: petunias, error, loading } = useAuthenticatedFetch<Petunia[]>('/api/petunias');

    if (loading) {
        return <p>Lade Petunien …</p>;
    }

    if (error) {
        return <p style={{ color: 'red' }}>Fehler: {error}</p>;
    }

    if (!petunias || petunias.length === 0) {
        return <p>Keine Petunien gefunden.</p>;
    }

    return (
        <div>
            <h1>Petunien</h1>
            <ul>
                {petunias.map((p, i) => (
                    <li key={i}>{p.name}</li>
                ))}
            </ul>
        </div>
    );
}

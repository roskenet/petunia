'use client';

import {useEffect, useState} from 'react';

type Petunia = {
    name: string;
    image_url: string;
};

export default function PetuniasPage() {
    const [petunias, setPetunias] = useState<Petunia[]>([]);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        fetch('http://localhost:8080/api/petunias', {
            credentials: 'include',
        })
            .then((res) => {
                if (!res.ok) throw new Error('Nicht eingeloggt oder Serverfehler');
                return res.json();
            })
            .then((data: Petunia[]) => setPetunias(data))
            .catch((err) => setError(err.message));
    }, []);

    if (error) {
        return <p style={{color: 'red'}}>Fehler: {error}</p>;
    }

    if (petunias.length === 0) {
        return <p>Lade Petunien …</p>;
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

'use client';

import React, { createContext, useContext, useState, ReactNode, useRef, useEffect, useCallback } from 'react';
import { apiBaseUrl } from '@/lib/api';

export type UserInfo = {
    name: string;
    email: string;
    roles: string[];
};

type UserContextType = {
    user: UserInfo | null;
    setUser: (user: UserInfo | null) => void;
    isLoading: boolean;
    setIsLoading: (loading: boolean) => void;
};

const UserContext = createContext<UserContextType | undefined>(undefined);

export function UserProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<UserInfo | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const fetchingRef = useRef(false);

    const loadUser = useCallback(async () => {
        if (fetchingRef.current) return;
        fetchingRef.current = true;
        
        setIsLoading(true);
        try {
            const response = await fetch(`${apiBaseUrl}/me`, {
                credentials: 'include',
            });
            if (!response.ok) throw new Error('Not authenticated');
            const data = await response.json();
            setUser(data);
        } catch (error) {
            setUser(null);
        } finally {
            setIsLoading(false);
            fetchingRef.current = false;
        }
    }, []);

    useEffect(() => {
        void loadUser();
    }, [loadUser]);

    return (
        <UserContext.Provider value={{ user, setUser, isLoading, setIsLoading }}>
            {children}
        </UserContext.Provider>
    );
}

export function useUser() {
    const context = useContext(UserContext);
    if (context === undefined) {
        throw new Error('useUser must be used within a UserProvider');
    }
    return context;
}

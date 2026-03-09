'use client';

import React, { createContext, useContext, useState, ReactNode } from 'react';

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

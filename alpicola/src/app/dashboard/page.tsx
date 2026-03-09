'use client'

import {useUser} from "@/context/UserContext";
import Paragraph from "antd/lib/typography/Paragraph";

export default function Dashboard() {

    const { user } = useUser();

    return (
        <div>

            <h1>Dashboard</h1>
            <Paragraph>Hello {user?.name}!</Paragraph>


        </div>
    );
}
import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
// import "antd/dist/antd.css";
import Header from "@/components/Header";

import { UserProvider } from "@/context/UserContext";
import { App, ConfigProvider } from "antd";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Petunia",
  description: "The official website of Petunia",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased`}
      >
      <ConfigProvider>
        <App>
          <UserProvider>
            <Header />
            <main>{children}</main>
          </UserProvider>
        </App>
      </ConfigProvider>
      </body>
    </html>
  );
}

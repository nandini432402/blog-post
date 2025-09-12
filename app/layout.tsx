import type React from "react"
import type { Metadata } from "next"
import { GeistSans } from "geist/font/sans"
import { GeistMono } from "geist/font/mono"
import { Analytics } from "@vercel/analytics/next"
import { BlogProvider } from "@/contexts/blog-context"
import { AuthProvider } from "@/contexts/auth-context"
import { Navbar } from "@/components/navbar"
import { Footer } from "@/components/footer"
import { Suspense } from "react"
import "./globals.css"

export const metadata: Metadata = {
  title: "BlogNest - Share Your Stories",
  description: "A modern blog platform built with Next.js 14 and TailwindCSS",
  generator: "v0.app",
}

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <html lang="en">
      <body className={`font-sans ${GeistSans.variable} ${GeistMono.variable}`}>
        <AuthProvider>
          <BlogProvider>
            <div className="min-h-screen flex flex-col">
              <Suspense fallback={<div>Loading...</div>}>
                <Navbar />
              </Suspense>
              <main className="flex-1">{children}</main>
              <Suspense fallback={<div>Loading...</div>}>
                <Footer />
              </Suspense>
            </div>
          </BlogProvider>
        </AuthProvider>
        <Analytics />
      </body>
    </html>
  )
}

"use client"

import type React from "react"
import { createContext, useContext, useState, useEffect } from "react"

interface User {
  id: string
  name: string
  email: string
  avatar?: string
  provider?: string // Added provider field to track login method
}

interface AuthContextType {
  user: User | null
  login: (email: string, password: string) => Promise<boolean>
  register: (name: string, email: string, password: string) => Promise<boolean>
  loginWithGoogle: () => Promise<boolean>
  loginWithGitHub: () => Promise<boolean>
  loginWithTwitter: () => Promise<boolean>
  logout: () => void
  isLoading: boolean
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    // Check for stored user session
    const storedUser = localStorage.getItem("blog-user")
    if (storedUser) {
      setUser(JSON.parse(storedUser))
    }
    setIsLoading(false)
  }, [])

  const login = async (email: string, password: string): Promise<boolean> => {
    setIsLoading(true)

    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 1000))

    // Simple validation - in real app, this would be server-side
    if (email && password.length >= 6) {
      const newUser: User = {
        id: Date.now().toString(),
        name: email.split("@")[0],
        email,
        avatar: `https://api.dicebear.com/7.x/avataaars/svg?seed=${email}`,
        provider: "email", // Added provider tracking
      }

      setUser(newUser)
      localStorage.setItem("blog-user", JSON.stringify(newUser))
      setIsLoading(false)
      return true
    }

    setIsLoading(false)
    return false
  }

  const register = async (name: string, email: string, password: string): Promise<boolean> => {
    setIsLoading(true)

    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 1000))

    // Simple validation
    if (name && email && password.length >= 6) {
      const newUser: User = {
        id: Date.now().toString(),
        name,
        email,
        avatar: `https://api.dicebear.com/7.x/avataaars/svg?seed=${email}`,
        provider: "email", // Added provider tracking
      }

      setUser(newUser)
      localStorage.setItem("blog-user", JSON.stringify(newUser))
      setIsLoading(false)
      return true
    }

    setIsLoading(false)
    return false
  }

  const loginWithGoogle = async (): Promise<boolean> => {
    setIsLoading(true)

    // Simulate OAuth flow
    await new Promise((resolve) => setTimeout(resolve, 1500))

    const mockGoogleUser: User = {
      id: `google_${Date.now()}`,
      name: "Google User",
      email: "user@gmail.com",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=google",
      provider: "google",
    }

    setUser(mockGoogleUser)
    localStorage.setItem("blog-user", JSON.stringify(mockGoogleUser))
    setIsLoading(false)
    return true
  }

  const loginWithGitHub = async (): Promise<boolean> => {
    setIsLoading(true)

    // Simulate OAuth flow
    await new Promise((resolve) => setTimeout(resolve, 1500))

    const mockGitHubUser: User = {
      id: `github_${Date.now()}`,
      name: "GitHub Developer",
      email: "developer@github.com",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=github",
      provider: "github",
    }

    setUser(mockGitHubUser)
    localStorage.setItem("blog-user", JSON.stringify(mockGitHubUser))
    setIsLoading(false)
    return true
  }

  const loginWithTwitter = async (): Promise<boolean> => {
    setIsLoading(true)

    // Simulate OAuth flow
    await new Promise((resolve) => setTimeout(resolve, 1500))

    const mockTwitterUser: User = {
      id: `twitter_${Date.now()}`,
      name: "Twitter User",
      email: "user@twitter.com",
      avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=twitter",
      provider: "twitter",
    }

    setUser(mockTwitterUser)
    localStorage.setItem("blog-user", JSON.stringify(mockTwitterUser))
    setIsLoading(false)
    return true
  }

  const logout = () => {
    setUser(null)
    localStorage.removeItem("blog-user")
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        register,
        loginWithGoogle,
        loginWithGitHub,
        loginWithTwitter,
        logout,
        isLoading,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider")
  }
  return context
}

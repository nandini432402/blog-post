"use client"


import { createContext, useContext, useState, type ReactNode } from "react" 
import { type BlogPost, initialPosts } from "@/lib/blog-data"

interface BlogContextType { posts: BlogPost[] 
  addPost: (post: Omit<BlogPost, "id" | "date">) => void 
  getPost: (id: string) => BlogPost | undefined }

const BlogContext = createContext<BlogContextType | undefined>(undefined)  

export function BlogProvider({ children }: { children: ReactNode }) { const [posts, setPosts] = useState<BlogPost[]>(initialPosts)

const addPost = (postData: Omit<BlogPost, "id" | "date">) => { const newPost: BlogPost = { ...postData, id: Date.now().toString(), date: new Date().toISOString().split("T")[0], } 
setPosts((prev) => [newPost, ...prev]) }  

const getPost = (id: string) => { return posts.find((post) => post.id === id) }

return <BlogContext.Provider value={{ posts, addPost, getPost }}>{children}</BlogContext.Provider> }

export function useBlog() { const context = useContext(BlogContext) 
  if (context === undefined) { throw new Error("useBlog must be used within a BlogProvider") } return context }
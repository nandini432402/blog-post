"use client"

import type React from "react"

import { useState } from "react"
import { useRouter } from "next/navigation"
import { useBlog } from "@/contexts/blog-context"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"
import { Label } from "@/components/ui/label"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { PenTool, ImageIcon, Tag, FileText } from "lucide-react"

export default function CreatePostPage() {
  const router = useRouter()
  const { addPost } = useBlog()

  const [formData, setFormData] = useState({
    title: "",
    content: "",
    featuredImage: "",
    tags: "",
  })

  const [isSubmitting, setIsSubmitting] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsSubmitting(true)

    try {
      // Create excerpt from content (first 150 characters)
      const excerpt = formData.content.length > 150 ? formData.content.substring(0, 150) + "..." : formData.content

      // Parse tags
      const tags = formData.tags
        .split(",")
        .map((tag) => tag.trim())
        .filter((tag) => tag.length > 0)

      // Add the post
      addPost({
        title: formData.title,
        content: formData.content,
        excerpt,
        featuredImage: formData.featuredImage || "/blog-post-concept.png",
        author: "Anonymous", // In a real app, this would come from auth
        tags,
      })

      // Redirect to home page
      router.push("/")
    } catch (error) {
      console.error("Error creating post:", error)
    } finally {
      setIsSubmitting(false)
    }
  }

  const handleChange = (field: string, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }))
  }

  return (
    <div className="container mx-auto px-4 py-8 max-w-2xl">
      <div className="text-center mb-8">
        <div className="flex items-center justify-center gap-2 mb-4">
          <PenTool className="h-8 w-8 text-primary" />
          <h1 className="text-3xl font-bold">Create New Post</h1>
        </div>
        <p className="text-muted-foreground">Share your thoughts, ideas, and stories with the community</p>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Post Details</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Title */}
            <div className="space-y-2">
              <Label htmlFor="title" className="flex items-center gap-2">
                <FileText className="h-4 w-4" />
                Title
              </Label>
              <Input
                id="title"
                placeholder="Enter your post title..."
                value={formData.title}
                onChange={(e) => handleChange("title", e.target.value)}
                required
              />
            </div>

            {/* Featured Image */}
            <div className="space-y-2">
              <Label htmlFor="featuredImage" className="flex items-center gap-2">
                <ImageIcon className="h-4 w-4" />
                Featured Image URL
              </Label>
              <Input
                id="featuredImage"
                type="url"
                placeholder="https://example.com/image.jpg (optional)"
                value={formData.featuredImage}
                onChange={(e) => handleChange("featuredImage", e.target.value)}
              />
              <p className="text-xs text-muted-foreground">Leave empty to use a default placeholder image</p>
            </div>

            {/* Tags */}
            <div className="space-y-2">
              <Label htmlFor="tags" className="flex items-center gap-2">
                <Tag className="h-4 w-4" />
                Tags
              </Label>
              <Input
                id="tags"
                placeholder="React, Next.js, Web Development (comma separated)"
                value={formData.tags}
                onChange={(e) => handleChange("tags", e.target.value)}
              />
              <p className="text-xs text-muted-foreground">Separate multiple tags with commas</p>
            </div>

            {/* Content */}
            <div className="space-y-2">
              <Label htmlFor="content">Content</Label>
              <Textarea
                id="content"
                placeholder="Write your post content here... You can use ## for headings and ### for subheadings."
                value={formData.content}
                onChange={(e) => handleChange("content", e.target.value)}
                rows={12}
                required
              />
              <p className="text-xs text-muted-foreground">Tip: Use ## for main headings and ### for subheadings</p>
            </div>

            {/* Submit Button */}
            <Button type="submit" className="w-full" disabled={isSubmitting || !formData.title || !formData.content}>
              {isSubmitting ? "Publishing..." : "Publish Post"}
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

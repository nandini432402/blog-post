"use client"

import { use } from "react"
import { notFound } from "next/navigation"
import Image from "next/image"
import { useBlog } from "@/contexts/blog-context"
import { Badge } from "@/components/ui/badge"
import { Card, CardContent, CardHeader } from "@/components/ui/card"
import { Separator } from "@/components/ui/separator"
import { Calendar, User, MessageCircle } from "lucide-react"

interface PostPageProps {
  params: Promise<{ id: string }>
}

export default function PostPage({ params }: PostPageProps) {
  const { id } = use(params)
  const { getPost } = useBlog()
  const post = getPost(id)

  if (!post) {
    notFound()
  }

  return (
    <div className="container mx-auto px-4 py-8 max-w-4xl">
      <article>
        {/* Featured Image */}
        <div className="aspect-video relative overflow-hidden rounded-lg mb-8">
          <Image
            src={post.featuredImage || "/placeholder.svg"}
            alt={post.title}
            fill
            className="object-cover"
            priority
          />
        </div>

        {/* Post Header */}
        <header className="mb-8">
          <div className="flex flex-wrap gap-2 mb-4">
            {post.tags.map((tag) => (
              <Badge key={tag} variant="secondary">
                {tag}
              </Badge>
            ))}
          </div>

          <h1 className="text-4xl font-bold mb-4 text-balance">{post.title}</h1>

          <div className="flex items-center gap-6 text-muted-foreground">
            <div className="flex items-center gap-2">
              <User className="h-4 w-4" />
              <span>{post.author}</span>
            </div>
            <div className="flex items-center gap-2">
              <Calendar className="h-4 w-4" />
              <span>
                {new Date(post.date).toLocaleDateString("en-US", {
                  year: "numeric",
                  month: "long",
                  day: "numeric",
                })}
              </span>
            </div>
          </div>
        </header>

        {/* Post Content */}
        <div className="prose prose-lg max-w-none mb-12">
          {post.content.split("\n").map((paragraph, index) => {
            if (paragraph.startsWith("## ")) {
              return (
                <h2 key={index} className="text-2xl font-semibold mt-8 mb-4">
                  {paragraph.replace("## ", "")}
                </h2>
              )
            }
            if (paragraph.startsWith("### ")) {
              return (
                <h3 key={index} className="text-xl font-semibold mt-6 mb-3">
                  {paragraph.replace("### ", "")}
                </h3>
              )
            }
            if (paragraph.startsWith("```")) {
              return null // Skip code block markers for now
            }
            if (paragraph.trim() === "") {
              return <br key={index} />
            }
            return (
              <p key={index} className="mb-4 text-pretty leading-relaxed">
                {paragraph}
              </p>
            )
          })}
        </div>

        <Separator className="my-8" />

        {/* Comments Section */}
        <section>
          <div className="flex items-center gap-2 mb-6">
            <MessageCircle className="h-5 w-5" />
            <h2 className="text-2xl font-semibold">Comments</h2>
          </div>

          <Card>
            <CardHeader>
              <h3 className="text-lg font-medium">Join the Discussion</h3>
            </CardHeader>
            <CardContent>
              <p className="text-muted-foreground">
                Comments are coming soon! We're working on building an engaging discussion platform where readers can
                share their thoughts and connect with the author and other community members.
              </p>
              <div className="mt-4 p-4 bg-muted rounded-lg">
                <p className="text-sm text-muted-foreground">
                  💡 <strong>Coming Soon:</strong> Real-time comments, reply threads, and community moderation features.
                </p>
              </div>
            </CardContent>
          </Card>
        </section>
      </article>
    </div>
  )
}

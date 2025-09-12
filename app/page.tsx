"use client"

import { useBlog } from "@/contexts/blog-context"
import { PostCard } from "@/components/post-card"

export default function HomePage() {
  const { posts } = useBlog()

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="text-center mb-12">
        <h1 className="text-4xl font-bold mb-4 text-balance">Welcome to BlogNest</h1>
        <p className="text-xl text-muted-foreground max-w-2xl mx-auto text-pretty">
          Discover amazing stories, insights, and ideas from our community of writers. Join the conversation and share
          your own experiences.
        </p>
      </div>

      {posts.length === 0 ? (
        <div className="text-center py-12">
          <p className="text-muted-foreground text-lg">No posts yet. Be the first to create one!</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {posts.map((post) => (
            <PostCard key={post.id} post={post} />
          ))}
        </div>
      )}
    </div>
  )
}

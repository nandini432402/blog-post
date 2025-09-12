import Link from "next/link"
import Image from "next/image"
import type { BlogPost } from "@/lib/blog-data"
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Calendar, User } from "lucide-react"

interface PostCardProps {
  post: BlogPost
}

export function PostCard({ post }: PostCardProps) {
  return (
    <Card className="overflow-hidden hover:shadow-lg transition-shadow">
      <div className="aspect-video relative overflow-hidden">
        <Image
          src={post.featuredImage || "/placeholder.svg"}
          alt={post.title}
          fill
          className="object-cover hover:scale-105 transition-transform duration-300"
        />
      </div>

      <CardHeader>
        <div className="flex flex-wrap gap-2 mb-2">
          {post.tags.slice(0, 3).map((tag) => (
            <Badge key={tag} variant="secondary" className="text-xs">
              {tag}
            </Badge>
          ))}
        </div>
        <h3 className="text-xl font-semibold line-clamp-2 text-balance">{post.title}</h3>
      </CardHeader>

      <CardContent>
        <p className="text-muted-foreground line-clamp-3 text-pretty">{post.excerpt}</p>

        <div className="flex items-center gap-4 mt-4 text-sm text-muted-foreground">
          <div className="flex items-center gap-1">
            <User className="h-4 w-4" />
            <span>{post.author}</span>
          </div>
          <div className="flex items-center gap-1">
            <Calendar className="h-4 w-4" />
            <span>{new Date(post.date).toLocaleDateString()}</span>
          </div>
        </div>
      </CardContent>

      <CardFooter>
        <Button asChild className="w-full">
          <Link href={`/post/${post.id}`}>Read More</Link>
        </Button>
      </CardFooter>
    </Card>
  )
}

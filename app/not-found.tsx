import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { FileQuestion, Home } from "lucide-react"

export default function NotFound() {
  return (
    <div className="container mx-auto px-4 py-16 max-w-2xl">
      <Card className="text-center">
        <CardHeader>
          <div className="flex justify-center mb-4">
            <FileQuestion className="h-16 w-16 text-muted-foreground" />
          </div>
          <CardTitle className="text-3xl">Post Not Found</CardTitle>
        </CardHeader>
        <CardContent>
          <p className="text-muted-foreground mb-6 text-pretty">
            Sorry, we couldn't find the blog post you're looking for. It may have been moved or deleted.
          </p>
          <Button asChild>
            <Link href="/" className="inline-flex items-center gap-2">
              <Home className="h-4 w-4" />
              Back to Home
            </Link>
          </Button>
        </CardContent>
      </Card>
    </div>
  )
}

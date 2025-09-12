import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Heart, Users, Zap, Globe } from "lucide-react"

export default function AboutPage() {
  const features = [
    {
      icon: <Zap className="h-6 w-6" />,
      title: "Fast & Modern",
      description: "Built with Next.js 14 and TailwindCSS for optimal performance and developer experience.",
    },
    {
      icon: <Users className="h-6 w-6" />,
      title: "Community Driven",
      description: "Connect with writers and readers from around the world. Share ideas and grow together.",
    },
    {
      icon: <Globe className="h-6 w-6" />,
      title: "Accessible",
      description: "Designed with accessibility in mind, ensuring everyone can enjoy great content.",
    },
    {
      icon: <Heart className="h-6 w-6" />,
      title: "Open Source",
      description: "Built in the open with modern web technologies and best practices.",
    },
  ]

  const techStack = ["Next.js 14", "React", "TailwindCSS", "TypeScript", "shadcn/ui"]

  return (
    <div className="container mx-auto px-4 py-8 max-w-4xl">
      {/* Hero Section */}
      <div className="text-center mb-12">
        <h1 className="text-4xl font-bold mb-4 text-balance">About BlogApp</h1>
        <p className="text-xl text-muted-foreground max-w-2xl mx-auto text-pretty">
          A modern, fast, and beautiful blogging platform designed for writers who want to share their stories with the
          world.
        </p>
      </div>

      {/* Mission Statement */}
      <Card className="mb-8">
        <CardHeader>
          <CardTitle className="text-2xl">Our Mission</CardTitle>
        </CardHeader>
        <CardContent className="prose prose-lg max-w-none">
          <p className="text-pretty leading-relaxed">
            BlogApp was created with a simple mission: to provide writers with a beautiful, fast, and intuitive platform
            to share their thoughts and connect with readers worldwide. We believe that everyone has a story worth
            telling, and we want to make it as easy as possible to tell it.
          </p>
          <p className="text-pretty leading-relaxed">
            Whether you're a seasoned blogger, a technical writer sharing tutorials, or someone just starting their
            writing journey, BlogApp provides the tools and community you need to succeed.
          </p>
        </CardContent>
      </Card>

      {/* Features Grid */}
      <div className="mb-8">
        <h2 className="text-2xl font-semibold mb-6 text-center">Why Choose BlogApp?</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {features.map((feature, index) => (
            <Card key={index}>
              <CardHeader>
                <div className="flex items-center gap-3">
                  <div className="p-2 bg-primary/10 rounded-lg text-primary">{feature.icon}</div>
                  <CardTitle className="text-lg">{feature.title}</CardTitle>
                </div>
              </CardHeader>
              <CardContent>
                <p className="text-muted-foreground text-pretty">{feature.description}</p>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>

      {/* Tech Stack */}
      <Card className="mb-8">
        <CardHeader>
          <CardTitle className="text-2xl">Built With Modern Technology</CardTitle>
        </CardHeader>
        <CardContent>
          <p className="text-muted-foreground mb-4 text-pretty">
            BlogApp is built using cutting-edge web technologies to ensure the best possible experience for both writers
            and readers:
          </p>
          <div className="flex flex-wrap gap-2">
            {techStack.map((tech) => (
              <Badge key={tech} variant="secondary" className="text-sm">
                {tech}
              </Badge>
            ))}
          </div>
        </CardContent>
      </Card>

      {/* Get Started */}
      <Card>
        <CardHeader>
          <CardTitle className="text-2xl">Get Started Today</CardTitle>
        </CardHeader>
        <CardContent>
          <p className="text-muted-foreground mb-4 text-pretty">
            Ready to start sharing your stories? Creating your first post is easy:
          </p>
          <ol className="list-decimal list-inside space-y-2 text-muted-foreground">
            <li>Click on "Create Post" in the navigation menu</li>
            <li>Fill in your post title, content, and tags</li>
            <li>Add a featured image URL (optional)</li>
            <li>Click "Publish Post" to share with the community</li>
          </ol>
          <p className="text-muted-foreground mt-4 text-pretty">
            Your post will immediately appear on the homepage for others to discover and enjoy. Join our growing
            community of writers and readers today!
          </p>
        </CardContent>
      </Card>
    </div>
  )
}

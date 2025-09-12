"use client"

export interface BlogPost {
  id: string
  title: string
  content: string
  excerpt: string
  featuredImage: string
  author: string
  date: string
  tags: string[]
}

export const initialPosts: BlogPost[] = [
  {
    id: "1",
    title: "Getting Started with Next.js 14",
    content: `Next.js 14 brings exciting new features and improvements to the React ecosystem. The App Router has matured significantly, offering better performance and developer experience.

In this comprehensive guide, we'll explore the key features that make Next.js 14 a game-changer for modern web development. From server components to improved routing, there's a lot to unpack.

## Key Features

### Server Components
Server Components allow you to render components on the server, reducing the JavaScript bundle size and improving performance. This is particularly beneficial for data-heavy applications.

### Improved Routing
The App Router provides a more intuitive file-based routing system with support for layouts, loading states, and error boundaries.

### Enhanced Performance
With optimizations in bundling and rendering, Next.js 14 delivers faster page loads and better Core Web Vitals scores.

## Getting Started

To create a new Next.js 14 project, simply run:

\`\`\`bash
npx create-next-app@latest my-app
\`\`\`

This will set up a new project with all the latest features and best practices built-in.`,
    excerpt:
      "Explore the exciting new features and improvements in Next.js 14, including Server Components, improved routing, and enhanced performance optimizations.",
    featuredImage: "/next-js-14-development.jpg",
    author: "Sarah Johnson",
    date: "2024-01-15",
    tags: ["Next.js", "React", "Web Development", "JavaScript"],
  },
  {
    id: "2",
    title: "Mastering TailwindCSS for Modern UI Design",
    content: `TailwindCSS has revolutionized how we approach CSS in modern web development. Its utility-first approach allows for rapid prototyping and consistent design systems.

## Why TailwindCSS?

TailwindCSS offers several advantages over traditional CSS frameworks:

### Utility-First Approach
Instead of writing custom CSS, you compose designs using utility classes. This leads to more maintainable and predictable styling.

### Customization
Tailwind is highly customizable through its configuration file, allowing you to create unique design systems while maintaining consistency.

### Performance
With its purge feature, Tailwind removes unused CSS, resulting in smaller bundle sizes and faster load times.

## Best Practices

1. **Use semantic class names** for complex components
2. **Leverage Tailwind's spacing scale** for consistent layouts
3. **Utilize responsive prefixes** for mobile-first design
4. **Create custom components** for repeated patterns

## Advanced Techniques

### Custom Utilities
You can extend Tailwind with custom utilities for project-specific needs:

\`\`\`css
@layer utilities {
  .text-balance {
    text-wrap: balance;
  }
}
\`\`\`

This approach ensures your custom styles integrate seamlessly with Tailwind's existing utilities.`,
    excerpt:
      "Learn how to leverage TailwindCSS's utility-first approach for creating beautiful, maintainable, and performant user interfaces.",
    featuredImage: "/tailwindcss-design-system.jpg",
    author: "Mike Chen",
    date: "2024-01-10",
    tags: ["TailwindCSS", "CSS", "UI Design", "Frontend"],
  },
  {
    id: "3",
    title: "Building Scalable React Applications",
    content: `As React applications grow in complexity, maintaining scalability becomes crucial. This guide covers essential patterns and practices for building large-scale React applications.

## Architecture Patterns

### Component Composition
Favor composition over inheritance. Build small, focused components that can be combined to create complex UIs.

### State Management
Choose the right state management solution based on your application's needs:

- **useState/useReducer** for local component state
- **Context API** for sharing state across component trees
- **External libraries** like Zustand or Redux for complex global state

## Performance Optimization

### Code Splitting
Use dynamic imports and React.lazy() to split your application into smaller chunks:

\`\`\`jsx
const LazyComponent = React.lazy(() => import('./LazyComponent'));
\`\`\`

### Memoization
Leverage React.memo, useMemo, and useCallback to prevent unnecessary re-renders:

\`\`\`jsx
const MemoizedComponent = React.memo(({ data }) => {
  const processedData = useMemo(() => 
    expensiveOperation(data), [data]
  );
  
  return <div>{processedData}</div>;
});
\`\`\`

## Testing Strategies

A robust testing strategy is essential for scalable applications:

1. **Unit tests** for individual components and functions
2. **Integration tests** for component interactions
3. **End-to-end tests** for critical user flows

## Folder Structure

Organize your code in a way that scales:

\`\`\`
src/
  components/
    ui/
    features/
  hooks/
  utils/
  types/
  __tests__/
\`\`\`

This structure promotes maintainability and makes it easier for teams to collaborate.`,
    excerpt:
      "Discover essential patterns and practices for building maintainable, performant, and scalable React applications that can grow with your team.",
    featuredImage: "/react-application-architecture.jpg",
    author: "Emily Rodriguez",
    date: "2024-01-05",
    tags: ["React", "Architecture", "Performance", "Best Practices"],
  },
]

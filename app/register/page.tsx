import { RegisterForm } from "@/components/auth/register-form"
import type { Metadata } from "next"

export const metadata: Metadata = {
  title: "Registration - Create Your Account",
  description: "Join our blog community by creating your account. Share your thoughts and connect with other writers.",
}

export default function RegistrationPage() {
  return <RegisterForm />
}

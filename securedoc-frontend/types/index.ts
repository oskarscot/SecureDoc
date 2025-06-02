import { z } from 'zod'
import {loginSchema} from "@/lib/schemas.ts";

export type LoginSchemaType = z.infer<typeof loginSchema>

export type UserType = {
    id: string
    username: string
    email: string
    createdAt: Date
    updatedAt: Date
}
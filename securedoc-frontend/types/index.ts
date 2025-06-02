import { z } from 'zod'
import {loginSchema} from "../src/lib/schemas";

export type LoginSchemaType = z.infer<typeof loginSchema>
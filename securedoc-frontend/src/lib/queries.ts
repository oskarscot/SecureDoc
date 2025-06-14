import {queryOptions} from "@tanstack/react-query";
import authHandler from "@/api/auth-handler.ts";
import type {LoginRequest} from "../../types";

export default function loginQuery(data: LoginRequest) {
  return queryOptions({
    queryKey: [''],
    queryFn: () => authHandler.login(data)
  })
}
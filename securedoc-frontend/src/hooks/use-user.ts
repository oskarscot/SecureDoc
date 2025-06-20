import * as React from 'react'
import { useCookies } from 'react-cookie'
import type {UserType} from "../../types";
import {BASE_URL} from "@/lib/constants.ts";

export function useUser() {
  const [user, setUser] = React.useState<UserType>()
  const [loading, setLoading] = React.useState<boolean>(true)
  const [cookies, setCookie] = useCookies(['user', 'auth'])

  React.useEffect(() => {
    const fetchUser = async () => {
      if(cookies.user) {
        return { user: cookies.user, loading: false }
      }
      try {
        const response = await fetch(`${BASE_URL}/users/me`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${cookies.auth.accessToken}`,
            },
        })
        const responseData = await response.json()
        setUser(responseData.resource)
        setCookie('user', responseData.resource, { path: '/' })
      } catch (error) {
        console.error('Failed to fetch user:', error)
      } finally {
        setLoading(false)
      }
    }

    fetchUser()
  }, [])

  return { user, loading }
}

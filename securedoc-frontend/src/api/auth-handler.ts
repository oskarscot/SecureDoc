import {BASE_URL} from '@/lib/constants.ts'

type LoginRequest = {
  email: string
  password: string
}

type LoginResponse = {
  resource: {
    accessToken: string
    refreshToken: string
  }
}

const authHandler = {
  async login(loginRequest: LoginRequest): Promise<LoginResponse> {
    const { email, password } = loginRequest
    const response = await fetch(`${BASE_URL}/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, password }),
    })

    if (!response.ok) {
      //TODO handle error properly
      throw new Error('Login failed')
    }

    return await response.json()
  },
}

export default authHandler

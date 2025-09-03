const API_URL = 'http://localhost:8080/api/v1'

async function request(endpoint, options = {}) {
  const url = `${API_URL}${endpoint}`
  const token = localStorage.getItem('token')
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  }

  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }

  const response = await fetch(url, {
    ...options,
    headers,
  })

  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`)
  }

  return response.json()
}

export default {
  get(endpoint) {
    return request(endpoint)
  },
  post(endpoint, data) {
    return request(endpoint, {
      method: 'POST',
      body: JSON.stringify(data),
    })
  },
  put(endpoint, data) {
    return request(endpoint, {
      method: 'PUT',
      body: JSON.stringify(data),
    })
  },
  delete(endpoint) {
    return request(endpoint, {
      method: 'DELETE',
    })
  },
}

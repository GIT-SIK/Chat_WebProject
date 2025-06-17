import { EventSourcePolyfill } from 'event-source-polyfill'

export function subscribeToSse(userUuid, onMessageCallback, onErrorCallback) {
  const token = localStorage.getItem('access_token')
  const headers = {
    Authorization: `Bearer ${token}`,
  }

  const EventSource = EventSourcePolyfill

  const sse = new EventSource(
    `http://localhost:8081/event/notification/subscribe?userUuid=${userUuid}`,
    {
      headers: headers,
      withCredentials: true,
    },
  )

  sse.onopen = () => {}

  sse.onmessage = (event) => {
    const notification = JSON.parse(event.data)
    if (onMessageCallback) {
      onMessageCallback(notification)
    }
  }

  sse.onerror = (error) => {
    if (onErrorCallback) {
      onErrorCallback(error)
    }
    sse.close()
  }

  return sse
}

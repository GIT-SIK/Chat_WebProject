import { EventSourcePolyfill } from "event-source-polyfill";

export function subscribeToSse(userId, onMessageCallback, onErrorCallback) {
    const token = localStorage.getItem('access_token')
    const headers = {
        "Authorization": `Bearer ${token}`
    };
    
    const EventSource = EventSourcePolyfill; 
    
    const sse = new EventSource(`http://localhost:8081/api/auth/notification/subscribe?userId=${userId}`, {
      headers: headers,
      withCredentials: true
    });


    sse.onopen = () => {
      console.log("SSE 연결 성공");
    };
  
    sse.onmessage = (event) => {
      const notification = JSON.parse(event.data);
      console.log(notification)
      if (onMessageCallback) {
        onMessageCallback(notification);
      }
    };

    sse.onerror = (error) => {
      if (onErrorCallback) {
        onErrorCallback(error);
      }
      sse.close();
    };
  
    return sse; 
  }
  
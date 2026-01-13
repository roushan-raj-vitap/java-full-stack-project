import { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

export default function ChatComponent({ currentUserId, chatWithUserId }) {
  const [messages, setMessages] = useState([]);
  const [text, setText] = useState("");
  let stompClient;

  useEffect(() => {
    const socket = new SockJS("http://localhost:8080/ws-chat");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
      stompClient.subscribe(`/topic/messages/${currentUserId}`, (msg) => {
        const received = JSON.parse(msg.body);
        setMessages((prev) => [...prev, received]);
      });
    });

    return () => stompClient.disconnect();
  }, [currentUserId]);

  const sendMessage = () => {
    stompClient.send(
      "/app/sendMessage",
      {},
      JSON.stringify({
        senderId: currentUserId,
        receiverId: chatWithUserId,
        content: text
      })
    );
    setMessages((prev) => [...prev, { senderId: currentUserId, content: text }]);
    setText("");
  };

  return (
    <div>
      {messages.map((m, i) => (
        <p key={i}><b>{m.senderId === currentUserId ? "You" : "Them"}:</b> {m.content}</p>
      ))}
      <input value={text} onChange={(e) => setText(e.target.value)} />
      <button onClick={sendMessage}>Send</button>
    </div>
  );
}

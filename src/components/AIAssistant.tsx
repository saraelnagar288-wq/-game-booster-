import { useState, useRef, useEffect } from "react";
import { Send, Bot, User, Loader2 } from "lucide-react";
import { useDeviceAnalyzer } from "../lib/useDeviceAnalyzer";
import { cn } from "../lib/utils";
import { motion } from "motion/react";

type Message = { role: "user" | "ai"; text: string };

export default function AIAssistant() {
  const device = useDeviceAnalyzer();
  const [messages, setMessages] = useState<Message[]>([
    { role: "ai", text: "Hello! I am GameBoost AI. Ask me about optimizing your games, understanding your device hardware, or fixing performance drops." }
  ]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);
  const scrollRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (scrollRef.current) {
      scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
    }
  }, [messages]);

  const handleSend = async () => {
    if (!input.trim() || loading) return;
    
    const userText = input.trim();
    setMessages(prev => [...prev, { role: "user", text: userText }]);
    setInput("");
    setLoading(true);

    try {
      const res = await fetch("/api/ai/ask", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          prompt: userText,
          context: device
        })
      });
      const data = await res.json();
      
      if (data.error) throw new Error(data.error);

      setMessages(prev => [...prev, { role: "ai", text: data.text }]);
    } catch (e: any) {
      setMessages(prev => [...prev, { role: "ai", text: `Error: ${e.message}. Please check API configuration or connection.` }]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex-1 flex flex-col h-[calc(100dvh-64px)] bg-neutral-950">
      <div className="p-4 border-b border-neutral-900 bg-neutral-950/80 backdrop-blur-md sticky top-0 z-10 flex items-center gap-3">
        <div className="w-10 h-10 bg-cyan-500/10 rounded-full flex items-center justify-center border border-cyan-500/20">
          <Bot className="text-cyan-400" size={20} />
        </div>
        <div>
          <h1 className="font-bold text-white">AI Assistant</h1>
          <p className="text-[10px] text-cyan-400 font-medium tracking-widest uppercase">Powered by Gemini</p>
        </div>
      </div>

      <div className="flex-1 overflow-y-auto p-4 space-y-4" ref={scrollRef}>
        {messages.map((msg, i) => (
          <motion.div 
            key={i}
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            className={cn(
              "flex gap-3 max-w-[85%]",
              msg.role === "user" ? "ml-auto flex-row-reverse" : ""
            )}
          >
            <div className={cn(
              "w-8 h-8 rounded-full flex items-center justify-center shrink-0 mt-1",
              msg.role === "user" ? "bg-neutral-800 text-neutral-400" : "bg-cyan-500/20 text-cyan-400"
            )}>
              {msg.role === "user" ? <User size={14} /> : <Bot size={14} />}
            </div>
            <div className={cn(
              "p-3 rounded-2xl text-sm leading-relaxed whitespace-pre-wrap",
              msg.role === "user" ? "bg-neutral-800 text-white rounded-tr-sm" : "bg-neutral-900 border border-neutral-800 text-neutral-200 rounded-tl-sm"
            )}>
              {msg.text}
            </div>
          </motion.div>
        ))}
        {loading && (
          <div className="flex gap-3 max-w-[85%]">
             <div className="w-8 h-8 rounded-full flex items-center justify-center shrink-0 mt-1 bg-cyan-500/20 text-cyan-400">
              <Bot size={14} />
            </div>
            <div className="p-3 rounded-2xl bg-neutral-900 border border-neutral-800 text-neutral-400 rounded-tl-sm flex items-center gap-2">
               <Loader2 size={16} className="animate-spin" /> Thinking...
            </div>
          </div>
        )}
      </div>

      <div className="p-4 bg-neutral-950 border-t border-neutral-900">
        <div className="relative">
          <input 
            type="text" 
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && handleSend()}
            placeholder="Ask about performance, hardware..."
            className="w-full bg-neutral-900 border border-neutral-800 rounded-full pl-4 pr-12 py-3 text-sm focus:outline-none focus:border-cyan-500 transition-colors"
          />
          <button 
            onClick={handleSend}
            disabled={!input.trim() || loading}
            className="absolute right-1 top-1 w-10 h-10 bg-cyan-500 text-black rounded-full flex items-center justify-center disabled:opacity-50 disabled:bg-neutral-800 disabled:text-neutral-500 transition-colors"
          >
            <Send size={16} className="ml-0.5" />
          </button>
        </div>
      </div>
    </div>
  );
}

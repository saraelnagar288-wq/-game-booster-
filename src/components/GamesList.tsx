import { useState } from "react";
import { Link } from "react-router-dom";
import { GAMES_DB } from "../lib/gamesDb";
import { Search, ChevronRight } from "lucide-react";
import { motion } from "motion/react";

export default function GamesList() {
  const [search, setSearch] = useState("");

  const filtered = GAMES_DB.filter(g => g.name.toLowerCase().includes(search.toLowerCase()));

  return (
    <div className="flex-1 flex flex-col h-full bg-neutral-950">
      <div className="p-4 pt-8 sticky top-0 bg-neutral-950/80 backdrop-blur-md z-10 border-b border-neutral-900">
        <h1 className="text-2xl font-bold mb-4">Games Library</h1>
        <div className="relative">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-neutral-500" size={18} />
          <input 
            type="text" 
            placeholder="Search games..." 
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full bg-neutral-900 border border-neutral-800 rounded-xl pl-10 pr-4 py-3 text-sm focus:outline-none focus:border-cyan-500 transition-colors"
          />
        </div>
      </div>

      <div className="p-4 pb-24 overflow-y-auto space-y-3">
        {filtered.map((game, i) => (
          <motion.div
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: i * 0.05 }}
            key={game.id}
          >
            <Link 
              to={`/games/${game.id}`}
              className="bg-neutral-900 border border-neutral-800 rounded-xl p-3 flex items-center gap-4 hover:border-neutral-700 transition-colors"
            >
              <img src={game.iconUrl} alt={game.name} className="w-14 h-14 rounded-lg object-cover bg-neutral-800" />
              <div className="flex-1 min-w-0">
                <h3 className="font-semibold text-neutral-100 truncate">{game.name}</h3>
                <p className="text-xs text-neutral-500 truncate">{game.package}</p>
                <div className="flex items-center gap-2 mt-1">
                  <span className="text-[10px] uppercase font-bold tracking-wider text-cyan-400 bg-cyan-400/10 px-2 py-0.5 rounded">
                    {game.engine}
                  </span>
                </div>
              </div>
              <ChevronRight className="text-neutral-600" size={20} />
            </Link>
          </motion.div>
        ))}

        {filtered.length === 0 && (
          <div className="text-center py-12 text-neutral-500">
            No games found.
          </div>
        )}
      </div>
    </div>
  );
}

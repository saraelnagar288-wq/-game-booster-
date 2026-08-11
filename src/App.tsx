/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import BottomNav from "./components/BottomNav";
import Dashboard from "./components/Dashboard";
import GamesList from "./components/GamesList";
import GameOptimizer from "./components/GameOptimizer";
import AIAssistant from "./components/AIAssistant";
import Monitor from "./components/Monitor";
import Settings from "./components/Settings";

export default function App() {
  return (
    <Router>
      <div className="w-full max-w-md bg-neutral-950 min-h-[100dvh] flex flex-col relative shadow-2xl border-x border-neutral-900/50">
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/games" element={<GamesList />} />
          <Route path="/games/:id" element={<GameOptimizer />} />
          <Route path="/monitor" element={<Monitor />} />
          <Route path="/ai" element={<AIAssistant />} />
          <Route path="/settings" element={<Settings />} />
        </Routes>
        <BottomNav />
      </div>
    </Router>
  );
}

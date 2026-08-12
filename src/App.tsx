/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import BottomNav from "./components/BottomNav";
import FeatureNav from "./components/FeatureNav";
import Dashboard from "./components/Dashboard";
import GamesList from "./components/GamesList";
import GameOptimizer from "./components/GameOptimizer";
import AIAssistant from "./components/AIAssistant";
import Monitor from "./components/Monitor";
import Settings from "./components/Settings";
import Performance from "./components/Performance";
import FPSMonitor from "./components/FPSMonitor";
import SystemPanel from "./components/SystemPanel";

export default function App() {
  return (
    <Router>
      <div className="w-full max-w-md bg-neutral-950 min-h-[100dvh] flex flex-col relative shadow-2xl border-x border-neutral-900/50">
        <FeatureNav />
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/performance" element={<Performance />} />
          <Route path="/games" element={<GamesList />} />
          <Route path="/games/:id" element={<GameOptimizer />} />
          <Route path="/fps" element={<FPSMonitor />} />
          <Route path="/monitor" element={<Monitor />} />
          <Route path="/ai" element={<AIAssistant />} />
          <Route path="/battery" element={<SystemPanel kind="battery" />} />
          <Route path="/thermal" element={<SystemPanel kind="thermal" />} />
          <Route path="/settings" element={<Settings />} />
        </Routes>
        <BottomNav />
      </div>
    </Router>
  );
}

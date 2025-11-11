import { QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { queryClient } from './config/queryClient.config';
import { useWebSocket } from './api/websocket/useWebSocket';
import MainLayout from './components/layout/MainLayout';
import {ToastContainer} from './components/notifications/ToastContainer';
import Dashboard from './pages/Dashboard';
import FlightManagement from './pages/FlightManagement/FlightManagement';
import GateManagement from './pages/GateManagement/GateManagement';
import AutomataView from './pages/AutomataView/AutomataView';
import ProcessDiagramView from './pages/ProcessDiagramView/ProcessDiagramView';
import AssignmentsView from './pages/AssignmentsView/AssignmentsView';
import MonitoringView from './pages/MonitoringView/MonitoringView';
import { useEffect } from 'react';

function AppContent() {
    const { status } = useWebSocket();

    useEffect(() => {
        console.log('[App] WebSocket Status:', status);
    }, [status]);

    return (
        <BrowserRouter>
            <MainLayout>
                <Routes>
                    <Route path="/" element={<Dashboard />} />
                    <Route path="/dashboard" element={<Dashboard />} />
                    <Route path="/vuelos" element={<FlightManagement />} />
                    <Route path="/gates" element={<GateManagement />} />
                    <Route path="/automata" element={<AutomataView />} />
                    <Route path="/process-diagram" element={<ProcessDiagramView />} />
                    <Route path="/asignaciones" element={<AssignmentsView />} />
                    <Route path="/monitoreo" element={<MonitoringView />} />

                    <Route path="*" element={<Navigate to="/" replace />} />
                </Routes>
            </MainLayout>

            <ToastContainer />
        </BrowserRouter>
    );
}

function App() {
    return (
        <QueryClientProvider client={queryClient}>
            <AppContent />
            <ReactQueryDevtools initialIsOpen={false} />
        </QueryClientProvider>
    );
}

export default App;

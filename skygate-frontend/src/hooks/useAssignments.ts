import { useQuery } from '@tanstack/react-query';
import { assignmentService } from '../api/services/assignment.service';

export function useAssignments() {
    const assignmentsQuery = useQuery({
        queryKey: ['assignments'],
        queryFn: () => assignmentService.getAll(false),
        refetchInterval: 5000,
    });

    const activeAssignmentsQuery = useQuery({
        queryKey: ['assignments', 'active'],
        queryFn: () => assignmentService.getActive(),
        refetchInterval: 3000,
    });

    return {
        assignments: assignmentsQuery.data?.data || [],
        activeAssignments: activeAssignmentsQuery.data?.data || [],
        isLoading: assignmentsQuery.isLoading || activeAssignmentsQuery.isLoading,
        isError: assignmentsQuery.isError || activeAssignmentsQuery.isError,
        error: assignmentsQuery.error || activeAssignmentsQuery.error,
    };
}

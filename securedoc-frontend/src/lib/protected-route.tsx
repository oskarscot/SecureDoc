import { Outlet, useNavigate} from "react-router-dom";
import {useEffect} from "react";
import {useCookies} from "react-cookie";

interface ProtectedRouteProps {
    children?: React.ReactNode;
    redirectPath?: string;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, redirectPath = '/login' }) => {
    const navigate = useNavigate();
    const [cookies] = useCookies(['auth']);
    const isAuthenticated = cookies.auth !== undefined; // TODO TEMP REMOVE ME!!!!

    useEffect(() => {
        if (!isAuthenticated) {
            navigate(redirectPath, { replace: true });
        }
    })

    return children ? <>{children}</> : <Outlet />;
}

export default ProtectedRoute;
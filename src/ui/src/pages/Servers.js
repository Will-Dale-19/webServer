import React, {useEffect, useState} from 'react'
import Buttons from "../components/Buttons";
import useToken from "../components/useToken";

const Servers = () => {
    const [servers, setServers] = useState([])
    const token = useToken().token;

    useEffect(() => {
        let route = ''
        token === "admin" ? route = '/api/servers' : route = '/api/servers/getUserServers';
        const getServers = async () => {
                const res = await fetch(route);
                const servers = await res.json();
                if (!fetching) {
                    setServers(servers);
                }
        }
        let fetching = false
        getServers().catch(e => {
            console.log('error fetching servers: ' + e);
        })
        return () => {
            fetching = true
        }
    },[token])

    if(token) {
        return (
            <div>
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Server Name</th>
                        <th>Server Location</th>
                    </tr>
                    </thead>
                    <tbody>
                    {servers.map(server => {
                        const {
                            serverId,
                            serverName,
                            serverLocation
                        } = server;
                        return (
                            <tr key={serverId}>
                                <td>{serverId}</td>
                                <td>{serverName}</td>
                                <td>{serverLocation}</td>
                                <td><Buttons server={serverName}/></td>
                            </tr>
                        )
                    })}
                    </tbody>
                </table>
            </div>

        )
    } else {
        throw new Error("Unauthorized Access Error");
    }
}

export default Servers
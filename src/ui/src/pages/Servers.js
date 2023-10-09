import React, { useLayoutEffect, useState } from 'react'
import Buttons from "../components/Buttons";
import useToken from "../components/useToken";

const Servers = () => {
    const [servers, setServers] = useState([])
    const [userServers, setUserServers] = useState([])
    const token = useToken().token;

    useLayoutEffect(() => {
        const getServers = async () => {
            const res = await fetch('/api/servers');
            const servers = await res.json();
            setServers(servers);
        }
        getServers().catch(e => {
            console.log('error fetching servers: ' + e);
        })
    })

    useLayoutEffect(() => {
        const getServers = async () => {
            const res = await fetch('/api/servers/getUserServers', {
                method: 'POST',
                body: JSON.stringify(token)
            });
            const userServers = await res.json();
            setUserServers(userServers);
        }
        getServers().catch(e => {
            console.log('error fetching user servers: ' + e);
        })
    })

    if(token) {
        return (
            <div>
                <table>
                    <thead>
                    <th>ID</th>
                    <th>Server Name</th>
                    <th>Server Location</th>
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
                            </tr>
                        )
                    })}
                    </tbody>
                </table>
                <Buttons/>
            </div>

        )
    } else {
        throw new Error("Unauthorized Access Error");
    }

}

export default Servers
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
        // checking to see if we have an admin token, if so get all servers.
        let serversToGet
        token === "admin" ? serversToGet = servers : serversToGet = userServers;

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
                    {serversToGet.map(server => {
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
                                <td><Buttons/></td>
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
import './Servers.css'
import React, {useEffect, useState} from 'react'
import Buttons from "../components/Buttons";
import useToken from "../components/useToken";

const checkError = (response) => {
    if(response.status !== 200){
        showApiError();
    }
    return response;
}

function showApiError() {
    throw new Error("Api Error");
}

const Servers = () => {
    const [servers, setServers] = useState(null)

    const token = useToken().token;

    useEffect(() => {
        let route = ''
        token === "admin" ? route = '/api/servers' : route = '/api/servers/getUserServers';
        const getServers = async () => {
            const res = token === "admin" ? await fetch(route) : await fetch(route, {
                method: 'POST',
                body: JSON.stringify(token)
            })

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
                <div>
                <ul>
                    <li>ID</li>
                    <li>Server Name</li>
                    <li>Server Location</li>
                </ul>
                </div>
                <div>
                {MapServers(servers)}
                </div>
            </div>

        )
    } else {
        throw new Error("Unauthorized Access Error");
    }

    function MapServers(servers) {
        if (!servers){
            return (
                <p>Loading...</p>
            )
        }
        return servers.map(server => {
            const {
                serverId,
                serverName,
                serverLocation,
                serverStatus
            } = server;
            return (
                <div>
                    <ul key={serverId}>
                        <li>{serverId}</li>
                        <li>{serverName}</li>
                        <li>{serverLocation}</li>
                        <li>{serverStatus}</li>
                        <li><Buttons server={serverName} serverStatus={serverStatus}/></li>
                    </ul>
                </div>
            )
        })
    }
}

export default Servers
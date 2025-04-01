import {useUserStore} from "@/stores/user.ts";

export async function login(credentials : Object): Promise<any> {

    return fetch(`http://localhost:8080/api/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(credentials)
    })
        .then(checkError)
        .then(data => data.json())
}

export async function getServers(): Promise<any> {
    return useUserStore().token === "admin" ? adminGetAll() : getUserServers(useUserStore().username)
        .then(checkError)

}

export async function getServerStatus(serverName : string): Promise<any> {
    return fetch(`http://localhost:8080/api/servers/getServerStatus/` + serverName, {
        method: 'GET'
    })
        .then(checkError)
}

export async function startServer(serverName: string): Promise<any> {
    return fetch(`http://localhost:8080/api/servers/startServer`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(serverName)
    })
        .then(checkError)
        .then(data => data.json())
}

export async function stopServer(serverName: string): Promise<any> {
    return fetch(`http://localhost:8080/api/servers/stopServer`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(serverName)
    })
        .then(checkError)
        .then(data => data.json())
}

function adminGetAll()  {
    return fetch('http://localhost:8080/api/servers', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

function getUserServers(username : string) {
    return fetch('http://localhost:8080/api/servers/getUserServers', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(username)
    })
}

const checkError = (response : Response) => {
    if(response.status !== 200){
        showApiError();
    }
    return response;
}

function showApiError() {
    throw new Error("Api Error");
}
const apiBaseUrl = process.env.NEXT_PUBLIC_API_BASE_URL ?? "";

export async function authenticatedFetch(path: string, init?: RequestInit): Promise<Response> {
  const response = await fetch(`${apiBaseUrl}${path}`, {
    credentials: "include",
    ...init,
    headers: {
      "Content-Type": "application/json",
      ...(init?.headers ?? {}),
    },
  });

  if (response.status === 401 || response.redirected) {
    window.location.href = `${apiBaseUrl}/oauth2/authorization/keycloak`;
    // We throw to stop the execution flow in the calling code
    throw new Error("Not authenticated");
  }

  return response;
}

export async function requestJson<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await authenticatedFetch(path, init);

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || `Request failed with status ${response.status}`);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return (await response.json()) as T;
}

export async function logout(): Promise<void> {
  window.location.href = `${apiBaseUrl}/logout`;
}

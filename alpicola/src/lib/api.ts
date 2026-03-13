const apiBaseUrl = process.env.NEXT_PUBLIC_API_BASE_URL ?? "";

function getCookie(name: string): string | undefined {
  if (typeof document === "undefined") return undefined;
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop()?.split(";").shift();
  return undefined;
}

export async function authenticatedFetch(path: string, init?: RequestInit): Promise<Response> {
  const headers: Record<string, string> = {
    "Content-Type": "application/json",
    ...(init?.headers as Record<string, string> ?? {}),
  };

  const method = init?.method?.toUpperCase() ?? "GET";
  const isMutating = ["POST", "PUT", "DELETE", "PATCH"].includes(method);

  if (isMutating) {
    const csrfToken = getCookie("XSRF-TOKEN");
    if (csrfToken) {
      headers["X-XSRF-TOKEN"] = csrfToken;
    }
  }

  const response = await fetch(`${apiBaseUrl}${path}`, {
    credentials: "include",
    ...init,
    headers,
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
  if (typeof document === "undefined") return;

  const form = document.createElement("form");
  form.method = "POST";
  form.action = `${apiBaseUrl}/logout`;

  const csrfToken = getCookie("XSRF-TOKEN");
  if (csrfToken) {
    // Spring Security accepts CSRF token as _csrf form field for POST logouts.
    const csrfInput = document.createElement("input");
    csrfInput.type = "hidden";
    csrfInput.name = "_csrf";
    csrfInput.value = csrfToken;
    form.appendChild(csrfInput);
  }

  document.body.appendChild(form);
  form.submit();
}

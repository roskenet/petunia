{{- define "petunia.checkSecret" -}}
{{- if not (lookup "v1" "Secret" .Release.Namespace "petunia-shared") }}
{{- fail "Secret 'petunia' not found. Please install the 'secrets' chart first." }}
{{- end }}
{{- end }}

{{- define "petunia.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If the release name contains the chart name, it will be used directly.
*/}}
{{- define "petunia.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains .Release.Name $name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{- define "petunia.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{- define "petunia.labels" -}}
helm.sh/chart: {{ include "petunia.chart" . }}
{{ include "petunia.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{- define "petunia.selectorLabels" -}}
app.kubernetes.io/name: {{ include "petunia.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}


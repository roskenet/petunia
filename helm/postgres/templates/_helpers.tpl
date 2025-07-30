{{- define "petunia.checkSecret" -}}
{{- if not (lookup "v1" "Secret" .Release.Namespace "petunia") }}
{{- fail "Secret 'petunia' not found. Please install the 'secrets' chart first." }}
{{- end }}
{{- end }}

{{/*
Expand the name of the chart.
*/}}
{{- define "petunia-postgres.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a simpler name by just using the release name
*/}}
{{- define "postgres.fullname" -}}
{{ .Release.Name }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "petunia-postgres.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "petunia-postgres.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "petunia-postgres.labels" -}}
helm.sh/chart: {{ include "petunia-postgres.chart" . }}
{{ include "petunia-postgres.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "petunia-postgres.selectorLabels" -}}
app.kubernetes.io/name: {{ include "petunia-postgres.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}
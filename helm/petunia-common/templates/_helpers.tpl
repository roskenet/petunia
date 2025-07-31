{{- define "petunia.checkSecret" -}}
{{- if not (lookup "v1" "Secret" .Release.Namespace "petunia") }}
{{- fail "Secret 'petunia' not found. Please install the 'secrets' chart first." }}
{{- end }}
{{- end }}

{{- define "petunia.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{- define "petunia.fullname" -}}
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


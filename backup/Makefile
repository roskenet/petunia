RELEASE ?= polonium
CHART_DIR ?= ./charts/polonium
NAMESPACE ?= default
VALUES_FILE ?= values.yaml

.PHONY: install upgrade uninstall lint template

install:
	helm install $(RELEASE) $(CHART_DIR) -n $(NAMESPACE) -f $(VALUES_FILE)

upgrade:
	helm upgrade --install $(RELEASE) $(CHART_DIR) -n $(NAMESPACE) -f $(VALUES_FILE)

uninstall:
	helm uninstall $(RELEASE) -n $(NAMESPACE)

lint:
	helm lint $(CHART_DIR)

template:
	helm template $(RELEASE) $(CHART_DIR) -n $(NAMESPACE) -f $(VALUES_FILE)


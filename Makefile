# java-clean-architect Makefile
SHELL := /bin/bash

MODULES := shared-kernel domain application infrastructure api
COMMANDS := create-pr pr-review new-slice status-check

# Real escape bytes via $(shell printf) so plain `echo` renders colors in any shell.
GREEN := $(shell printf '\033[0;32m')
YELLOW := $(shell printf '\033[0;33m')
RED := $(shell printf '\033[0;31m')
NC := $(shell printf '\033[0m')

.PHONY: help build test verify run doctor

help:
	@echo "$(GREEN)java-clean-architect$(NC)"
	@echo "  make build    - compile all modules"
	@echo "  make test     - unit + ArchUnit tests"
	@echo "  make verify   - full build incl. integration tests (needs Docker)"
	@echo "  make run      - run the Spring Boot app (api module)"
	@echo "  make doctor   - validate agent structure (skills, commands, routing)"

build:
	@[ -x ./mvnw ] || { echo "$(RED)./mvnw not found — Java skeleton not scaffolded yet$(NC)"; exit 1; }
	./mvnw compile

test:
	@[ -x ./mvnw ] || { echo "$(RED)./mvnw not found — Java skeleton not scaffolded yet$(NC)"; exit 1; }
	./mvnw test

verify:
	@[ -x ./mvnw ] || { echo "$(RED)./mvnw not found — Java skeleton not scaffolded yet$(NC)"; exit 1; }
	./mvnw verify

run:
	@[ -x ./mvnw ] || { echo "$(RED)./mvnw not found — Java skeleton not scaffolded yet$(NC)"; exit 1; }
	./mvnw -pl api spring-boot:run

doctor:
	@ok=1; \
	[ -f CLAUDE.md ] || { echo "$(RED)x missing CLAUDE.md$(NC)"; ok=0; }; \
	[ -f agent_docs/skill-routing.md ] || { echo "$(RED)x missing agent_docs/skill-routing.md$(NC)"; ok=0; }; \
	[ -f .gitignore ] || { echo "$(YELLOW)! no .gitignore$(NC)"; }; \
	for s in platform $(MODULES); do \
		f=".claude/skills/$$s/SKILL.md"; \
		[ -f "$$f" ] || { echo "$(RED)x missing skill: $$f$(NC)"; ok=0; }; \
	done; \
	for c in $(COMMANDS); do \
		f=".claude/commands/$$c.md"; \
		[ -f "$$f" ] || { echo "$(RED)x missing command: $$f$(NC)"; ok=0; }; \
	done; \
	if grep -rql '<fill in>' .claude/skills/ 2>/dev/null; then \
		echo "$(YELLOW)! placeholders (<fill in>) remain in some skills$(NC)"; fi; \
	for m in $(MODULES); do \
		[ -d "$$m" ] || echo "$(YELLOW)! module '$$m/' not scaffolded yet (Java skeleton pending)$(NC)"; \
	done; \
	if [ $$ok -eq 1 ]; then echo "$(GREEN)ok doctor: structure OK (warnings above, if any, are reminders)$(NC)"; \
	else echo "$(RED)doctor: structural problems found$(NC)"; exit 1; fi

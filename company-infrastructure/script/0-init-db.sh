#!/bin/bash


psql -U postgres -tc "SELECT 1 FROM pg_database WHERE datname = 'company'" | grep -q 1 || psql -U postgres -c "CREATE DATABASE company"

psql -U postgres -tc "SELECT 1 FROM pg_database WHERE datname = 'company_test'" | grep -q 1 || psql -U postgres -c "CREATE DATABASE company_test"

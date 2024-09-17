# WebSight JCR Maintenance

## Introduction

This project provides reference implementation of Maintenance jobs for maintaining a Apache
Jackrabbit OAK repository in Apache Sling.

It was originally a fork
of [Apache Sling JCR Maintenance](https://github.com/apache/sling-org-apache-sling-jcr-maintenance).

## Configuration

The reference configuration is available
in the [Configuration Feature](https://github.com/websight-io/websight-jcr-maintenance/blob/master/src/main/features/configuration.json).

## Changes

Changes done on the original implementation are:

- Giving a possibility to disable each job. This change prevents unnecessary health check warnings
  when a job is not required, ensuring that only the relevant jobs are activated in a given
  application setup.
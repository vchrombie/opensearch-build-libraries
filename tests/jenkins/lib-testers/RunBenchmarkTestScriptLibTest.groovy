/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */
import static org.hamcrest.CoreMatchers.notNullValue
import static org.hamcrest.MatcherAssert.assertThat

class RunBenchmarkTestScriptLibTester extends LibFunctionTester{

    private String bundleManifest
    private String insecure
    private String workload
    private String singleNode
    private String minDistribution
    private String use50PercentHeap
    private String enableRemoteStore
    private String suffix
    private String managerNodeCount
    private String dataNodeCount
    private String clientNodeCount = ''
    private String ingestNodeCount = ''
    private String mlNodeCount = ''
    private String dataInstanceType
    private String userTag
    private String workloadParams
    private String additionalConfig
    private String dataStorageSize = '200'
    private String mlStorageSize = '200'
    private String jvmSysProps = ''
    private String captureNodeStat
    private String captureSegmentReplicationStat
    private String telemetryParams

    public RunBenchmarkTestScriptLibTester(bundleManifest, insecure, workload, singleNode, minDistribution, use50PercentHeap,
                                           enableRemoteStore, managerNodeCount, dataNodeCount, dataInstanceType, userTag, workloadParams,
                                           additionalConfig, captureNodeStat, captureSegmentReplicationStat, telemetryParams){
        this.bundleManifest = bundleManifest
        this.insecure = insecure
        this.workload = workload
        this.singleNode = singleNode
        this.minDistribution = minDistribution
        this.use50PercentHeap = use50PercentHeap
        this.enableRemoteStore = enableRemoteStore
        this.managerNodeCount = managerNodeCount
        this.dataNodeCount = dataNodeCount
        this.dataInstanceType = dataInstanceType
        this.userTag = userTag
        this.workloadParams = workloadParams
        this.additionalConfig = additionalConfig
        this.captureNodeStat = captureNodeStat
        this.captureSegmentReplicationStat = captureSegmentReplicationStat
        this.telemetryParams = telemetryParams
    }


    @Override
    String libFunctionName() {
        return 'runBenchmarkTestScript'
    }

    @Override
    void parameterInvariantsAssertions(Object call) {
        assertThat(call.args.bundleManifest.first(), notNullValue())
        if (!this.insecure.isEmpty()) {
            assertThat(call.args.insecure.first(), notNullValue())
        }
        if (!this.workload.isEmpty()) {
            assertThat(call.args.workload.first(), notNullValue())
        }
    }

    @Override
    boolean expectedParametersMatcher(Object call) {
        return call.args.bundleManifest.first().toString().equals(this.bundleManifest)
    }

    @Override
    void configure(Object helper, Object binding) {
        helper.registerAllowedMethod("s3Download", [Map])
        helper.registerAllowedMethod("uploadTestResults", [Map])
        helper.registerAllowedMethod("s3Upload", [Map])
        helper.registerAllowedMethod("withAWS", [Map, Closure], {
            args,
            closure ->
                closure.delegate = delegate
                return helper.callClosure(closure)
        })
        helper.registerAllowedMethod('findFiles', [Map.class], null)
        helper.registerAllowedMethod("withCredentials", [Map])
        helper.registerAllowedMethod("downloadBuildManifest", [Map], {
            c -> lib.jenkins.BuildManifest.new(readYaml(file: bundleManifest))
        })
        helper.registerAllowedMethod('parameterizedCron', [String], null)
        binding.setVariable('AGENT_LABEL', 'Jenkins-Agent-AL2-X64-C54xlarge-Docker-Host')
        binding.setVariable('AGENT_IMAGE', 'opensearchstaging/ci-runner:ci-runner-centos7-v1')
        binding.setVariable('ARCHITECTURE', 'x64')
        binding.setVariable('ARTIFACT_BUCKET_NAME', 'test_bucket')
        binding.setVariable('ARTIFACT_DOWNLOAD_ROLE_NAME', 'Dummy_Download_Role')
        binding.setVariable('AWS_ACCOUNT_PUBLIC', 'dummy_account')
        binding.setVariable('env', ['BUILD_NUMBER': '307'])
        binding.setVariable('BUILD_NUMBER', '307')
        binding.setVariable('BUILD_URL', 'test://artifact.url')
        binding.setVariable('BUNDLE_MANIFEST', bundleManifest)
        binding.setVariable('BUNDLE_MANIFEST_URL', 'test://artifact.url')
        binding.setVariable('GITHUB_BOT_TOKEN_NAME', 'bot_token_name')
        binding.setVariable('GITHUB_USER', 'test_user')
        binding.setVariable('GITHUB_TOKEN', 'test_token')
        binding.setVariable('HAS_SECURITY', insecure)
        binding.setVariable('SINGLE_NODE_CLUSTER', singleNode)
        binding.setVariable('MIN_DISTRIBUTION', minDistribution)
        binding.setVariable('USE_50_PERCENT_HEAP', use50PercentHeap)
        binding.setVariable('ENABLE_REMOTE_STORE', enableRemoteStore)
        binding.setVariable('SUFFIX', suffix)
        binding.setVariable('MANAGER_NODE_COUNT', managerNodeCount)
        binding.setVariable('DATA_NODE_COUNT', dataNodeCount)
        binding.setVariable('USER_TAGS', userTag)
        binding.setVariable('WORKLOAD_PARAMS', workloadParams)
        binding.setVariable('ADDITIONAL_CONFIG', additionalConfig)
        binding.setVariable('CLIENT_NODE_COUNT', clientNodeCount)
        binding.setVariable('INGEST_NODE_COUNT', ingestNodeCount)
        binding.setVariable('ML_NODE_COUNT', mlNodeCount)
        binding.setVariable('DATA_NODE_STORAGE', dataStorageSize)
        binding.setVariable('ML_NODE_STORAGE', mlStorageSize)
        binding.setVariable('DATA_INSTANCE_TYPE', dataInstanceType)
        binding.setVariable('JVM_SYS_PROPS', jvmSysProps)
        binding.setVariable('CAPTURE_NODE_STAT', captureNodeStat)
        binding.setVariable('CAPTURE_SEGMENT_REPLICATION_STAT', captureSegmentReplicationStat)
        binding.setVariable('JOB_NAME', 'benchmark-test')
        binding.setVariable('BENCHMARK_TEST_CONFIG_LOCATION', 'test_config')
        binding.setVariable('PUBLIC_ARTIFACT_URL', 'test://artifact.url')
        binding.setVariable('STAGE_NAME', 'test_stage')
        binding.setVariable('TEST_WORKLOAD', workload)
        binding.setVariable('WEBHOOK_URL', 'test://artifact.url')
        binding.setVariable('TELEMETRY_PARAMS', telemetryParams)
    }
}

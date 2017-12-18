package org.mule.modules.docker.automation.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class TestsConstants {

    // test specific constants
    public static final String IMAGE_NAME = "busybox";
    public static final String IMAGE_TAG = "latest";
    public static final List<String> COMMAND = Collections.unmodifiableList(Arrays.asList(new String[] {
        "sleep",
        "9999"
    }));

    public static final String START_CONTAINER = "start-container-name";

    public static final String INSPECT_CONTAINER = "inspect-container-name";
    public static final boolean INSPECT_CONTAINER_SHOW_SIZE = true;

    public static final String STOP_CONTAINER = "stop-container-name";
    public static final int STOP_CONTAINER_TIMEOUT = 10;

    public static final String RESTART_CONTAINER = "restart-container-name";
    public static final int RESTART_CONTAINER_TIMEOUT = 10;

    public static final String KILL_CONTAINER = "kill-container-name";
    public static final String KILL_CONTAINER_SIGNAL = "SIGKILL";

    public static final String PAUSE_CONTAINER = "pause-container-name";

    public static final String UNPAUSE_CONTAINER = "unpause-container-name";

    public static final String WAIT_A_CONTAINER = "wait-container-name";

    public static final String DELETE_CONTAINER = "delete-container-name";
    public static final boolean DELETE_CONTAINER_REMOVE_VOLUME = true;
    public static final boolean DELETE_CONTAINER_FORCE_REMOVE = true;

    public static final String RUN_CONTAINER = "run-container-name";
    public static final String RUN_CONTAINER_IMAGE_NAME = "run-container-image-name";
    public static final String RUN_CONTAINER_IMAGE_TAG = "run-container-image-name";
    public static final List<String> RUN_CONTAINER_COMMAND = Collections.unmodifiableList(Arrays.asList(new String[] {
        "sleep",
        "9999"
    }));

    public static final boolean LIST_CONTAINERS_SHOW_ALL = true;
    public static final String LIST_CONTAINERS_SHOW_BEFORE_CONTAINER = "container-name";
    public static final int LIST_CONTAINERS_LIMIT = 10;
    public static final String LIST_CONTAINERS_WITH_STATUS = "running";
    public static final boolean LIST_CONTAINERS_SHOW_SIZE = true;
    public static final String LIST_CONTAINERS_WITH_LABELS = "label-name";

    public static final String CREATE_CONTAINER = "create-container-name";
    public static final String CREATE_CONTAINERS_IMAGE = "busybox";
    public static final String CREATE_CONTAINERS_IMAGE_TAG = "latest";
    public static final String CREATE_CONTAINERS_JSON_FILE_PATH = "src/test/resources/input-files/create-container.json";

    public static final String GET_CONTAINER_LOG = "get-container-name-logs";
    public static final String GET_CONTAINER_LOG_IMAGE = "busybox";
    public static final String GET_CONTAINER_LOG_IMAGE_TAG = "latest";
    public static final boolean GET_CONTAINER_LOG_SHOW_TIME_STAMP = true;
    public static final boolean GET_CONTAINER_LOG_STANDARD_OUT = true;
    public static final boolean GET_CONTAINER_LOG_STANDARD_ERROR = true;
    public static final int GET_CONTAINER_LOG_SHOW_SINCE = 10;
    public static final int GET_CONTAINER_LOG_TAIL = 10;
    public static final boolean GET_CONTAINER_LOG_FOLLOW_LOGS = true;

    public static final String GET_CONTAINER_STATS = "get-container-stats-name";

    public static final String REMOVE_IMAGE_IMAGENAME = "hackmann/empty";
    public static final String REMOVE_IMAGE_IMAGETAG = "latest";
    public static final String REMOVE_IMAGE_IMAGEID = "4568897";
    public static final boolean REMOVE_IMAGE_FORCE_REMOVE = true;

    public static final String REMOVE_VOLUME_volumeName = "created-test-remove";

    public static final String TAG_IMAGE_NAME = "busybox";
    public static final String TAG_IMAGE_TAG = "latest";
    public static final String TAG_IMAGE_REPOSITORY = "localhost:5000";
    public static final String TAG_IMAGE_DEST_IMAGE_NAME = "test-tag-image";
    public static final String TAG_IMAGE_DEST_IMAGE_TAG = "test";

    public static final String BUILD_IMAGE_DOCKERFILE_PATH = "src/test/resources/input-files/Dockerfile";
    public static final String BUILD_IMAGE_CPUSET = "0";
    public static final String BUILD_IMAGE_CPUSHARES = "40";
    public static final String BUILD_IMAGE_BUILDARGUMET_NAME = "HTTP_PROXY";
    public static final String BUILD_IMAGE_BUILDARGUMET_VALUE = "http://localhost:5000";
    public static final String BUILD_IMAGE_REMOTEURI = "";
    public static final Map<String, String> BUILD_IMAGE_LABELS = ImmutableMap.of("TestKey1", "TestValue1", "TestKey2", "TestValue2");
    public static final long BUILD_IMAGE_MEMORY = 5000000;
    public static final long BUILD_IMAGE_MEMSWAP = 6000000;
    public static final List<String> BUILD_IMAGE_CACHE_FROM_IMAGE = Collections.unmodifiableList(Arrays.asList(new String[] {
        "ubuntu:latest"
    }));
    public static final Boolean BUILD_IMAGE_NOCACHE = false;
    public static final boolean BUILD_IMAGE_FORCERM = true;
    public static final boolean BUILD_IMAGE_PULLIMAGE = true;
    public static final boolean BUILD_IMAGE_REMOVE_CONTAINERS = true;

    public static final String CREATE_VOLUME_VOLUMENAME = "volume1";
    public static final String CREATE_VOLUME_VOLUMEDRIVER = "local";
    public static final Map<String, String> CREATE_VOLUME_DRIVEROPTS = ImmutableMap.of("type", "tmpfs");

    public static final String INSPECT_IMAGE_IMAGENAME = "busybox";
    public static final String INSPECT_IMAGE_IMAGETAG = "latest";

    public static final String INSPECT_VOLUME_VOLUME_NAME = "testVolume";
    public static final String INSPECT_VOLUME_VOLUMEDRIVER = "local";
    public static final Map<String, String> INSPECT_VOLUME_DRIVEROPTS = ImmutableMap.of("type", "tmpfs");

    public static final String LIST_VOLUME_VOLUME_NAME = "volume1";
    public static final String LIST_VOLUME_VOLUMEDRIVER = "local";
    public static final boolean LIST_VOLUME_DANGLING_FILTER = false;
    public static final Map<String, String> LIST_VOLUME_DRIVEROPTS = ImmutableMap.of("type", "tmpfs");

    public static final String PULL_IMAGE_TESTIMAGE = "busybox";
    public static final String PULL_IMAGE_IMAGETAG = "latest";
    public static final String PULL_IMAGE_USERNAME = null;
    public static final String PULL_IMAGE_PASSWORD = null;

    public static final boolean LIST_IMAGE_SHOWALL = true;
    public static final boolean LIST_IMAGE_DANGLING = false;
    public static final String LIST_IMAGE_IMAGENAME_FILTER = "list-image-filter";
    public static final String LIST_IMAGE_IMAGELABEL_FILTER = "image-label-filter";

    public static final String PUSH_IMAGE_IMAGETAG = "latest";
    public static final String PUSH_IMAGE_REPOSITORY = "localhost:5000";
    public static final String PUSH_IMAGE_DEST_IMAGE_NAME = "test-tag-image";
    public static final String PUSH_IMAGE_DEST_IMAGE_TAG = "test";
    public static final boolean PUSH_IMAGE_PRUNE = false;
    public static final String PUSH_IMAGE_USERNAME = "testuser";
    public static final String PUSH_IMAGE_PASSWORD = "testpassword";

    public static final String REMOVE_VOLUME_VOLUMENAME = "created-test-remove";

}

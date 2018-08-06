FILESEXTRAPATHS_prepend := "${RDK_ARCHIVE_PATH}:"

require recipes-extended/dpdk/dpdk.inc

STABLE = "-stable"
BRANCH = "17.08"
SRCREV = "02657b4adcb8af773e26ec061b01cd7abdd3f0b6"

SRC_URI = "git://dpdk.org/dpdk${STABLE};branch=${BRANCH} \
           file://dpdk-16.04-add-RTE_KERNELDIR_OUT-to-split-kernel-bu.patch \
           file://${RDK_DPDK_PATCH} \
           file://examples-eventdev-pipeline-Fix-implicit-declaration.patch "

LICENSE = "LGPLv2 & GPLv2 & BSD"
LIC_FILES_CHKSUM = "file://LICENSE.GPL;md5=751419260aa954499f7abaabaa882bbe \
                    file://LICENSE.LGPL;md5=4fbd65380cdd255951079008b364516c \
                    file://LICENSE.BSD;md5=701db1808cef1c74d6d226be6fa5be17"

DEPENDS_append_axxiax86-64 = " rdk-user"

PACKAGECONFIG_append_axxiax86-64 = " numa"

export SYSROOT = "${WORKDIR}/recipe-sysroot"
export LIB_CPKAE_DIR = "${SYSROOT}/usr/lib"
export IES_API_DIR = "${SYSROOT}/usr/lib"
export LIB_QAT18_DIR = "${SYSROOT}/usr/lib"

# Disable devel build to avoid rpath in binaries.
# Default is y if sources are in a git tree.
export RTE_DEVEL_BUILD = "n"

# make sure compile target support SSE4.2 for auto-vectorizing
TARGET_CFLAGS_append_axxiax86-64 = "-msse4.2 "

do_configure_prepend () {
	# Set RDK config options in RTE_TARGET defconfig
	RTE_DEFCONFIG="${S}/config/defconfig_${RTE_TARGET}"
	echo "CONFIG_RTE_BUILD_SHARED_LIB=y"               >> ${RTE_DEFCONFIG}
	echo "CONFIG_RTE_MAX_MEMZONE=10240"                >> ${RTE_DEFCONFIG}
	echo "CONFIG_RTE_EAL_NUMA_AWARE_HUGEPAGES=y"       >> ${RTE_DEFCONFIG}
	echo "CONFIG_RTE_MAX_QUEUES_PER_PORT=16384"        >> ${RTE_DEFCONFIG}
	echo "CONFIG_RTE_LIBRTE_PMD_PCAP=y"                >> ${RTE_DEFCONFIG}
	echo "CONFIG_RTE_LIBRTE_PMD_IHQM_EVENTDEV_DEBUG=y" >> ${RTE_DEFCONFIG}
	echo "CONFIG_RTE_LIBRTE_PMD_QAT=y"                 >> ${RTE_DEFCONFIG}
	echo "CONFIG_RTE_LIBRTE_PMD_QAT_DEBUG_DRIVER=y"    >> ${RTE_DEFCONFIG}
	echo "CONFIG_RTE_LIBRTE_POWER=y"                   >> ${RTE_DEFCONFIG}
	echo "CONFIG_RTE_LIBRTE_VHOST_NUMA=y"              >> ${RTE_DEFCONFIG}
}

do_install_append () {
	install -m 755 ${S}/${RTE_TARGET}/app/dpdk-pmdinfogen ${D}${bindir}
	install -m 755 ${S}/examples/ethtool/lib/${RTE_TARGET}/lib/librte_ethtool.so.1 \
		       ${D}${libdir}
	ln -sf librte_ethtool.so.1 ${D}${libdir}/librte_ethtool.so

	rm -rf ${D}/usr/share/mk
	rm -rf ${D}/usr/share/${RTE_TARGET}/app

	chown -R root:root ${D}
}

RDEPENDS_${PN}-examples += "${PN} ${PN}-dev"

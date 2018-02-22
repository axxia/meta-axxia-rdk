meta-intel-axxia-rdk
==============

This layer adds build instructions to the meta-intel-axxia layer to create an
'in-distribution' image.

When applied, meta-intel-axxia-rdk provides support for creating a distribution that
adds out-of-tree RDK kernel level modules to the resulting image. The RDK kernel
modules also get loaded on boot-up.

Use of this layer assumes that you are being supplied with the relevant kernel
module source code release archives directly from Intel.

Updates to meta-intel-axxia README
============================

This section contains updates and additions to the meta-intel-axxia README, needed 
to apply this layer.

## Sources

### In-Distribution builds
For in-distribution builds, the Intel github.com repositories provide two layer
variants : meta-intel-axxia-rdk and meta-intel-axxia-rdk_private.  Unless
instructed otherwise, the publicly-available meta-intel-axxia-rdk should be
used to provide in-distribution support. 

```
git clone https://github.com/axxia/meta-intel-axxia-rdk.git
```

The private repository is used for development and is not supported.
To access the private repository, request permission from Intel. 

```
git clone https://github.com/axxia/meta-intel-axxia-rdk_private.git meta-intel-axxia-rdk
```

In all cases, use the 'rocko' branch or the tag specified in the release notes.


## Building the meta-intel-axxia BSP layer

3.1: Clone the Axxia RDK meta layer. This provides meta data for building
in-distribution images for the Axxia specific board types.  See 'Sources' above to
select the right meta-intel-axxia repository, branch, and version.

```
   $ cd $YOCTO/poky
   $ <the git clone command chosen above>
   $ cd meta-intel-axxia-rdk
   $ git checkout rocko (or git checkout tags/<tag from the release notes>)
   $ mkdir downloads
   $ cd downloads
   $ cp /your/path/to/rdk_klm_src_<releaseinfo>.txz .
   $ ln -s rdk_klm_src_<releaseinfo>.txz rdk_klm_src.tar.xz
```

8:  Edit the conf/bblayers.conf file

```
   $ pwd (you should be at $YOCTO/axxia)
   $ vi conf/bblayers.conf
```

Edit BBLAYERS variable as follows. Replace references to $YOCTO below with the
actual value you provided in step 1.

```
   BBLAYERS ?= " \
            $YOCTO/poky/meta \
            $YOCTO/poky/meta-poky \
            $YOCTO/poky/meta-openembedded/meta-oe \
            $YOCTO/poky/meta-openembedded/meta-python \
            $YOCTO/poky/meta-openembedded/meta-networking \
            $YOCTO/poky/meta-virtualization \
            $YOCTO/poky/meta-intel \
            $YOCTO/poky/meta-intel-axxia \
            $YOCTO/poky/meta-intel-axxia/meta-intel-snr \
            $YOCTO/poky/meta-intel-axxia-rdk \
            "
```
9: Edit the conf/local.conf file:

```
   $ vi conf/local.conf
```

9.1: Set distribution configuration to have all Axxia specific features.

To build an in-distribution image:

    DISTRO = "intel-axxia-indist"

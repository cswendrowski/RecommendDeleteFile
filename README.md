RecommendDeleteFile
===================
Recommend: Delete File (or RDF) is a program inspired by windirstat. RDF recursively searches through your harddrives, solid state drives, usbs, and more to find large files that have not been used in a long time. By default, the program will search your C:/ drive for files that are over 1gb in size and have not been accessed in over 30 days. The location, size, and last access time can all be set by the user. Multiple locations can be searched, and will be queued up if more than one are entered at a time. The resulting files found are displayed, and the user can quickly find them in the file system with a button press. These files can be blacklisted, which removes them from showing up in search. The blacklist can also block file extension types.

The implementation of RDF results in an extremely light-weight program, at only 0.02 MB. Only 1 file outside of the .jar is created (the blacklist). The speed of the program is directly tied to the CPU power of the computer.

Created by Cody Swendrowski
cody@swendrowski.us

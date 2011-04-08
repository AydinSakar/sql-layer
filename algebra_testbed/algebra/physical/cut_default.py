#
# Copyright (C) 2011 Akiban Technologies Inc.
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License, version 3,
# as published by the Free Software Foundation.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see http://www.gnu.org/licenses.
#

import physicaloperator

SimpleOperator = physicaloperator.SimpleOperator

class Cut(SimpleOperator):

    def __init__(self, input, rowtype):
        SimpleOperator.__init__(self, input)
        self._rowtype = rowtype

    def handle_row(self, row):
        if self._rowtype.ancestor_of(row.rowtype):
            output_row = None
        else:
            output_row = row
        return output_row
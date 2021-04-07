var mongoose = require('mongoose');
var Schema = mongoose.Schema;

const compressionTherapySchema = new mongoose.Schema({
    startTime: { type: String, required: true },
    duration: { type: String, required: true },
    endTime: { type: String, required: true },
    daynightTime: { type: String, required: true},
    name: { type: String, required: true},
    user: {
        type: Schema.Types.ObjectId, ref: 'User'
    }
}, { timestamps: true });

const CompressionTherapy = mongoose.model('CompressionTherapy', compressionTherapySchema);

module.exports = CompressionTherapy
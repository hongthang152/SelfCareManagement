var mongoose = require('mongoose');
var Schema = mongoose.Schema;

const manualLymphDrainageMassageSchema = new mongoose.Schema({
    startTime: { type: String, required: true },
    duration: { type: String, required: true },
    endTime: { type: String, required: true },
    user: {
        type: Schema.Types.ObjectId, ref: 'User'
    }
}, { timestamps: true });

const ManualLymphDrainageMassage = mongoose.model('ManualLymphDrainageMassage', manualLymphDrainageMassageSchema);

module.exports = ManualLymphDrainageMassage